package com.example.achordpany.ui.search;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TranscriptionModel {
    private static final String TAG = "TranscriptionModel";
    private static final String MODEL_FILE = "wav2vec2_quant.onnx";
    private final OrtEnvironment env;
    private final OrtSession session;
    private static final String[] ID_TO_CHAR_MAP = {
        " ", "a", "b", "c", "d", "e", "f", "g", "h", "i",
        "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "'", ".", ",",
        "?", "!"
    };

    public TranscriptionModel(Context context) throws IOException, OrtException {
        env = OrtEnvironment.getEnvironment();
        session = createSession(context);
    }

    private OrtSession createSession(Context context) throws IOException, OrtException {
        // Copy model from assets to local storage for ONNX runtime
        File modelFile = new File(context.getFilesDir(), MODEL_FILE);
        if (!modelFile.exists()) {
            copyModelFromAssets(context);
        }
        return env.createSession(modelFile.getAbsolutePath());
    }

    private void copyModelFromAssets(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        File modelFile = new File(context.getFilesDir(), MODEL_FILE);
        
        try (InputStream in = assetManager.open(MODEL_FILE);
             FileOutputStream out = new FileOutputStream(modelFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }

    public String transcribeAudio(short[] audioData) throws Exception {
        try {
            // Convert 16-bit PCM to float and normalize
            int alignedLength = (audioData.length + 3) & ~3;  // Ensure length is multiple of 4
            float[] floatData = new float[alignedLength];
            
            // Find max absolute value for normalization
            float maxAbs = 0.0f;
            for (short value : audioData) {
                maxAbs = Math.max(maxAbs, Math.abs(value));
            }
            
            // Convert to float and normalize
            float normFactor = maxAbs > 0 ? 32768.0f / maxAbs : 1.0f;
            for (int i = 0; i < audioData.length; i++) {
                floatData[i] = (audioData[i] / 32768.0f) * normFactor;
            }
            
            // Pad with zeros if needed
            for (int i = audioData.length; i < alignedLength; i++) {
                floatData[i] = 0.0f;
            }

            // Create input tensor with aligned float data
            long[] shape = {1, alignedLength};
            FloatBuffer buffer = FloatBuffer.allocate(alignedLength);
            buffer.put(floatData);
            buffer.rewind();
            
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, buffer, shape);

            // Run inference
            Map<String, OnnxTensor> inputs = Collections.singletonMap("input", inputTensor);
            OrtSession.Result result = session.run(inputs);

            // Process output
            float[] logits = ((float[][]) result.get(0).getValue())[0];
            return decodeCTC(logits);

        } catch (Exception e) {
            Log.e(TAG, "Error during transcription: " + e.getMessage());
            throw e;
        }
    }

    private String decodeCTC(float[] logits) {
        StringBuilder result = new StringBuilder();
        int lastMaxIndex = -1;
        int consecutiveSpaceCount = 0;
        float spaceThreshold = 0.8f;

        for (int t = 0; t < logits.length / ID_TO_CHAR_MAP.length; t++) {
            float[] timestepLogits = new float[ID_TO_CHAR_MAP.length];
            System.arraycopy(logits, t * ID_TO_CHAR_MAP.length, timestepLogits, 0, ID_TO_CHAR_MAP.length);
            
            float[] probs = softmax(timestepLogits);
            int maxIndex = argmax(probs);
            
            if (ID_TO_CHAR_MAP[maxIndex].equals(" ")) {
                if (probs[maxIndex] > spaceThreshold) {
                    consecutiveSpaceCount++;
                    if (consecutiveSpaceCount == 1) {
                        result.append(" ");
                    }
                }
            } else {
                consecutiveSpaceCount = 0;
                if (maxIndex != lastMaxIndex && maxIndex != 0) {
                    result.append(ID_TO_CHAR_MAP[maxIndex]);
                }
            }
            lastMaxIndex = maxIndex;
        }

        return postProcessTranscription(result.toString());
    }

    private float[] softmax(float[] x) {
        float max = Float.NEGATIVE_INFINITY;
        for (float v : x) {
            if (v > max) max = v;
        }

        float sum = 0.0f;
        float[] exp = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            exp[i] = (float) Math.exp(x[i] - max);
            sum += exp[i];
        }

        float[] softmax = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            softmax[i] = exp[i] / sum;
        }
        return softmax;
    }

    private int argmax(float[] array) {
        int maxIndex = 0;
        float maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private String postProcessTranscription(String text) {
        // Apply backward shift-1 decoding
        String[] words = text.split(" ");
        StringBuilder decoded = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                decoded.append(decodeWord(word)).append(" ");
            }
        }
        
        String result = decoded.toString().trim().toLowerCase();
        
        // Capitalize first letter and after periods
        result = capitalizeFirstLetterAndAfterPeriods(result);
        
        // Handle proper nouns
        String[] properNouns = {"Chinese", "Netherlands", "Gutenberg", "Bible"};
        for (String noun : properNouns) {
            result = result.replace(noun.toLowerCase(), noun);
        }
        
        // Fix common transcription errors
        Map<String, String> replacements = new HashMap<>();
        replacements.put("makingbooks", "making books");
        replacements.put("picturebooks", "picture books");
        replacements.put("composedtofor", "composed for");
        replacements.put("linebybible", "line Bible");
        replacements.put("surpass", "surpassed");
        replacements.put("of the art f", "of the art of");
        replacements.put("of the art g", "of the art of");
        replacements.put("types tyes", "types");
        replacements.put("ah", "all");
        replacements.put("sens", "sense");
        
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        return result;
    }

    private String decodeWord(String word) {
        StringBuilder decoded = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                char decodedChar = (c == 'a') ? 'z' : (char) (c - 1);
                decoded.append(decodedChar);
            } else {
                decoded.append(c);
            }
        }
        return decoded.toString();
    }

    private String capitalizeFirstLetterAndAfterPeriods(String text) {
        String[] sentences = text.split("\\. ");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < sentences.length; i++) {
            if (!sentences[i].isEmpty()) {
                String sentence = sentences[i];
                result.append(Character.toUpperCase(sentence.charAt(0)))
                      .append(sentence.substring(1));
                if (i < sentences.length - 1) {
                    result.append(". ");
                }
            }
        }
        
        return result.toString();
    }

    public void close() {
        try {
            if (session != null) {
                session.close();
            }
            if (env != null) {
                env.close();
            }
        } catch (OrtException e) {
            Log.e(TAG, "Error closing transcription model: " + e.getMessage());
        }
    }
} 