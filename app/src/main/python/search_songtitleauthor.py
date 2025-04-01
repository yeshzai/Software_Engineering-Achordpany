import requests

def get_song_by_lyrics(lyrics):
    api_url = f"https://itunes.apple.com/search?term={lyrics}&entity=song&limit=5"
    response = requests.get(api_url)

    if response.status_code == 200:
        data = response.json()
        songs = [(track["trackName"]) for track in data.get("results", [])]
        return songs if songs else None
    else:
        return None