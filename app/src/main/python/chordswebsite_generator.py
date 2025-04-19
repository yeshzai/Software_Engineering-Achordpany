from googlesearch import search

def find_chords(song_title):
    # List of chord sites to try, in order of priority
    chord_sites = [
        "ultimate-guitar.com",
        "e-chords.com",
        "chordie.com",
        "guitartabs.cc",
        "chordify.net"
    ]

    for site in chord_sites:
        query = f'{song_title} guitar chords site:{site}'
        try:
            for result in search(query, num_results=1):
                return result  # Return the first valid result found
        except Exception as e:
            print(f"Error searching {site}: {e}")
            continue  # If search fails, try the next site

    return None  # If no result found on any site

