import os
from PIL import Image

def resize_and_replace(target_file, reference_file):
    try:
        # Apri il file di riferimento e il file di destinazione
        reference_img = Image.open(reference_file)
        target_img = Image.open(target_file)

        # Ottieni le dimensioni del file di destinazione
        target_size = target_img.size

        # Ridimensiona il file di riferimento alle dimensioni del file di destinazione
        resized_reference = reference_img.resize(target_size)

        # Sovrascrivi il file di destinazione con la nuova immagine ridimensionata
        resized_reference.save(target_file)
        print(f"Sostituito {target_file} con il file di riferimento ridimensionato.")
    except Exception as e:
        print(f"Errore durante la sostituzione di {target_file}: {e}")

def replace_images_in_directory(reference_file, directory=".", recursive=False):
    # Scansione della cartella
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.lower().endswith('.png') and file.lower() != os.path.basename(reference_file).lower():
                # Costruisci il percorso completo del file di destinazione
                target_file = os.path.join(root, file)
                resize_and_replace(target_file, reference_file)

        # Se non deve cercare in modo ricorsivo, esce dopo la prima cartella
        if not recursive:
            break

if __name__ == "__main__":
    # Imposta il file di riferimento
    reference_file = "appIcon.png"  # Il file di riferimento deve essere nella directory corrente

    # Usa la directory corrente per impostazione predefinita
    current_directory = os.getcwd()

    # Imposta la variabile recursive a True se vuoi cercare anche nelle sottocartelle
    replace_images_in_directory(reference_file, current_directory, recursive=False)
