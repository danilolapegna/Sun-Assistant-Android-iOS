import os
from PIL import Image

# Define the icon sizes for various devices and contexts
icon_sizes = {
    "iPhone_Notification": [(40, 40), (60, 60)],
    "iPhone_Settings": [(58, 58), (87, 87)],
    "iPhone_Spotlight": [(80, 80), (120, 120)],
    "iPhone_App": [(120, 120), (180, 180)],
    "iPad_Notifications": [(20, 20), (40, 40)],
    "iPad_Settings": [(29, 29), (58, 58)],
    "iPad_Spotlight": [(40, 40), (80, 80)],
    "iPad_App": [(76, 76), (152, 152)],
    "iPad_Pro_App": [(167, 167)]
}

def resize_icons(image_path, output_folder):
    base_name = os.path.basename(image_path)
    image_name, ext = os.path.splitext(base_name)

    try:
        with Image.open(image_path) as img:
            # Determine the resampling filter
            resampling_filter = Image.LANCZOS if hasattr(Image, 'LANCZOS') else Image.Resampling.LANCZOS
            
            for device, sizes in icon_sizes.items():
                for size in sizes:
                    resized_img = img.resize(size, resampling_filter)
                    new_image_name = f"{image_name}_{device}@{size[0]}x{size[1]}{ext}"
                    resized_img.save(os.path.join(output_folder, new_image_name))
    except IOError:
        print(f"Cannot resize {image_path}")

def main():
    script_folder = os.getcwd()  # Current working directory
    output_folder = os.path.join(script_folder, "output")  # Subfolder for output

    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    for filename in os.listdir(script_folder):
        if filename.lower().endswith(('.png', '.jpg', '.jpeg')):
            resize_icons(os.path.join(script_folder, filename), output_folder)

if __name__ == "__main__":
    main()
