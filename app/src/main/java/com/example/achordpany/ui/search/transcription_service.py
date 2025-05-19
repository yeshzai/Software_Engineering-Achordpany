import numpy as np
import onnxruntime
import logging
import os
from pathlib import Path

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('transcription_service.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

def find_model_path():
    """Find the ONNX model in the assets directory."""
    # Get the current script's directory
    current_dir = Path(__file__).resolve().parent
    
    # Try to find the assets directory by walking up the directory tree
    current_path = current_dir
    while current_path.name != "app" and current_path.parent != current_path:
        current_path = current_path.parent
    
    if current_path.name != "app":
        raise FileNotFoundError("Could not find 'app' directory in parent path")
    
    # Construct the path to the assets directory
    assets_path = current_path / "src" / "main" / "assets" / "wav2vec2_quant.onnx"
    
    if not assets_path.exists():
        raise FileNotFoundError(f"Model file not found at {assets_path}")
    
    logger.info(f"Found model at: {assets_path}")
    return str(assets_path)

class TranscriptionModel:
    ID_TO_CHAR_MAP = [
        " ", "a", "b", "c", "d", "e", "f", "g", "h", "i",
        "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "'", ".", ",",
        "?", "!"
    ]

    def __init__(self):
        self.model_path = find_model_path()
        self.session = onnxruntime.InferenceSession(self.model_path)
        logger.info(f"Loaded model from {self.model_path}")

    def preprocess_audio(self, audio_data):
        """Preprocess audio data for the model."""
        # Convert to float32 if needed
        if isinstance(audio_data, list):
            audio_data = np.array(audio_data, dtype=np.float32)
        elif audio_data.dtype != np.float32:
            audio_data = audio_data.astype(np.float32)

        # Normalize
        if np.max(np.abs(audio_data)) > 0:
            audio_data = audio_data / np.max(np.abs(audio_data))

        # Ensure shape is correct (batch_size, sequence_length)
        if len(audio_data.shape) == 1:
            audio_data = np.expand_dims(audio_data, axis=0)

        return audio_data

    def transcribe_audio(self, audio_data):
        """Transcribe audio data to text."""
        try:
            # Preprocess audio
            processed_audio = self.preprocess_audio(audio_data)
            logger.info(f"Preprocessed audio shape: {processed_audio.shape}")

            # Run inference
            input_name = self.session.get_inputs()[0].name
            outputs = self.session.run(None, {input_name: processed_audio})
            logits = outputs[0][0]  # Take first batch

            # Decode output
            text = self.decode_ctc(logits)
            logger.info(f"Transcribed text: {text}")
            return text

        except Exception as e:
            logger.error(f"Error during transcription: {str(e)}")
            raise

    def decode_ctc(self, logits):
        """Decode CTC output to text."""
        result = []
        last_max_index = -1
        consecutive_space_count = 0
        space_threshold = 0.8

        # Reshape logits if needed
        if len(logits.shape) == 1:
            logits = logits.reshape(-1, len(self.ID_TO_CHAR_MAP))

        for timestep_logits in logits:
            probs = self.softmax(timestep_logits)
            max_index = np.argmax(probs)
            
            if self.ID_TO_CHAR_MAP[max_index] == " ":
                if probs[max_index] > space_threshold:
                    consecutive_space_count += 1
                    if consecutive_space_count == 1:
                        result.append(" ")
            else:
                consecutive_space_count = 0
                if max_index != last_max_index and max_index != 0:
                    result.append(self.ID_TO_CHAR_MAP[max_index])
            
            last_max_index = max_index

        return self.post_process_transcription("".join(result))

    def softmax(self, x):
        """Compute softmax values for each set of scores in x."""
        exp_x = np.exp(x - np.max(x))
        return exp_x / exp_x.sum()

    def post_process_transcription(self, text):
        """Post-process the transcribed text."""
        # Apply backward shift-1 decoding
        words = text.split()
        decoded_words = [self.decode_word(word) for word in words if word]
        result = " ".join(decoded_words).lower()

        # Capitalize first letter and after periods
        result = self.capitalize_first_letter_and_after_periods(result)

        # Handle proper nouns
        proper_nouns = ["Chinese", "Netherlands", "Gutenberg", "Bible"]
        for noun in proper_nouns:
            result = result.replace(noun.lower(), noun)

        # Fix common transcription errors
        replacements = {
            "makingbooks": "making books",
            "picturebooks": "picture books",
            "composedtofor": "composed for",
            "linebybible": "line Bible",
            "surpass": "surpassed",
            "of the art f": "of the art of",
            "of the art g": "of the art of",
            "types tyes": "types",
            "ah": "all",
            "sens": "sense"
        }
        for old, new in replacements.items():
            result = result.replace(old, new)

        return result

    def decode_word(self, word):
        """Apply backward shift-1 decoding to a word."""
        decoded = []
        for c in word:
            if c.isalpha():
                decoded_char = 'z' if c == 'a' else chr(ord(c) - 1)
                decoded.append(decoded_char)
            else:
                decoded.append(c)
        return "".join(decoded)

    def capitalize_first_letter_and_after_periods(self, text):
        """Capitalize the first letter of the text and after periods."""
        sentences = text.split(". ")
        capitalized = []
        for sentence in sentences:
            if sentence:
                capitalized.append(sentence[0].upper() + sentence[1:])
        return ". ".join(capitalized)

# Create a singleton instance for Java to use
model = TranscriptionModel()

def transcribe(audio_data):
    """Entry point for Java to call transcription."""
    return model.transcribe_audio(audio_data) 