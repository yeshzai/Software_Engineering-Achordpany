from googlesearch import search

def find_chords(song_title, artist):
    query = f"{song_title} {artist} guitar chords site:ultimate-guitar.com"

    for result in search(query, num_results=1):
        return result  # Return the first search result

    return "No results found."