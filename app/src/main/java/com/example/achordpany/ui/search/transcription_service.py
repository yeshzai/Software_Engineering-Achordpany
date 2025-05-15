import numpy as np
import onnxruntime
from flask import Flask, request, jsonify
import logging
import os
from datetime import datetime

app = Flask(__name__)

# Configure logging with more detailed format
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('transcription.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class TranscriptionService:
    def __init__(self, model_path):
        self.env = onnxruntime.get_default_session()
        self.session = onnxruntime.InferenceSession(model_path)
        self.vocab_size = 32
        self.blank_token_id = 0
        self.id_to_char_map = [
            " ", "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "'", ".", ",",
            "?", "!"
        ]

    def process_audio(self, audio_data):
        try:
            # Ensure audio data is normalized
            max_abs = np.max(np.abs(audio_data))
            if max_abs > 0:
                audio_data = audio_data / max_abs

            # Prepare input tensor
            input_data = audio_data.reshape(1, -1).astype(np.float32)
            input_name = self.session.get_inputs()[0].name
            
            # Run inference
            result = self.session.run(None, {input_name: input_data})
            
            # Process output
            output_array = result[0]
            return self.decode_ctc_greedy(output_array[0])
            
        except Exception as e:
            logger.error(f"Error processing audio: {str(e)}")
            raise

    def decode_ctc_greedy(self, logits):
        time_steps = logits.shape[0]
        result = []
        last_max_index = -1

        for t in range(time_steps):
            max_index = np.argmax(logits[t])
            if max_index != last_max_index and max_index != self.blank_token_id:
                if max_index < len(self.id_to_char_map):
                    result.append(self.id_to_char_map[max_index])
            last_max_index = max_index

        return ''.join(result)

# Initialize the transcription service
model_path = os.path.join(os.path.dirname(__file__), "models", "wav2vec2_quant.onnx")
transcription_service = None

try:
    transcription_service = TranscriptionService(model_path)
    logger.info("Transcription service initialized successfully")
except Exception as e:
    logger.error(f"Failed to initialize transcription service: {str(e)}")

@app.route('/transcribe', methods=['POST'])
def transcribe():
    try:
        if not transcription_service:
            logger.error("Transcription request failed: Service not initialized")
            return jsonify({"error": "Transcription service not initialized"}), 500

        # Get audio data from request
        audio_data = np.frombuffer(request.data, dtype=np.float32)
        
        # Log request details
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        logger.info(f"Received transcription request at {timestamp}")
        logger.info(f"Audio data shape: {audio_data.shape}")
        
        # Process audio and get transcription
        transcription = transcription_service.process_audio(audio_data)
        
        # Log the transcription result
        logger.info(f"Transcription result: '{transcription}'")
        logger.info(f"Transcription length: {len(transcription)} characters")
        
        return jsonify({"transcription": transcription})
    
    except Exception as e:
        logger.error(f"Error during transcription: {str(e)}", exc_info=True)
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000) 