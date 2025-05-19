# Achordpany Local Transcription

This update moves the speech transcription functionality from a server-based approach to a local on-device implementation using ONNX Runtime for Android.

## Setup

1. Place the quantized model file `wav2vec2_quant.onnx` in the `app/src/main/assets` directory.
2. Ensure you have the following dependencies in your `build.gradle`:
   ```gradle
   implementation 'com.microsoft.onnxruntime:onnxruntime-android:1.15.1'
   ```

## Implementation Details

The transcription system consists of two main components:

1. `TranscriptionModel.java`: Handles the ONNX model loading and inference
   - Loads the model from assets
   - Performs audio preprocessing
   - Runs inference using ONNX Runtime
   - Implements CTC decoding and post-processing

2. `SongSearchActivity.java`: Manages audio recording and UI
   - Records audio at 16kHz mono
   - Converts audio to float format
   - Uses TranscriptionModel for transcription
   - Handles UI feedback and error states

## Model Requirements

- The ONNX model expects audio input as a float tensor of shape [1, sequence_length]
- Audio should be normalized to [-1, 1] range
- Sampling rate: 16kHz
- Input format: 16-bit PCM

## Error Handling

The implementation includes comprehensive error handling for:
- Model initialization failures
- Audio recording issues
- Transcription errors
- Empty or invalid results

## Performance Considerations

- The model runs entirely on-device, eliminating network latency
- Quantized model reduces memory footprint
- Automatic resource cleanup in onDestroy() 