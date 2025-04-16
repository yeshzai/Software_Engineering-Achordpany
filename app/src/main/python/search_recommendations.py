import random
import requests

def get_songs_by_genre(genre):
    api_url = f"https://itunes.apple.com/search?term={genre}&entity=song&limit=10"
    response = requests.get(api_url)

    if response.status_code == 200:
        data = response.json()
        songs = [(track["trackName"]) for track in data.get("results", [])]
        return random.sample(songs, min(5, len(songs))) if songs else None
    else:
        return None