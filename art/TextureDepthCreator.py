from PIL import Image
import numpy as np
from scipy.spatial.distance import cdist
import time
import os
from collections import defaultdict
from datetime import datetime
import subprocess
import psutil
import sys

# Global queue for processing
file_queue = defaultdict(lambda: {'last_modified': None, 'output_path': None, 'tolerance': None})

# Add at the top with other globals
MEMORY_THRESHOLD = 95  # Percentage of RAM usage that triggers kill
MEMORY_CHECK_INTERVAL = 0.2  # How often to check memory in seconds

def notify(title, message):
    """
    Send desktop notification and print with bell
    """
    print(f"\a{title}: {message}")  # \a is the bell character
    try:
        # os.system(f'notify-send "{title}" "{message}"')
        subprocess.run(['notify-send', title, message])
    except:
        pass  # Silently fail if notify-send isn't available

def monitor_and_process(check_interval=5):
    """
    Continuously monitors the file queue and processes files that have changed.
    """
    while True:
        files_to_process = []
        
        # Check each file in the queue
        for input_path, info in file_queue.items():
            try:
                current_mtime = os.path.getmtime(input_path)
                if info['last_modified'] != current_mtime:
                    files_to_process.append((input_path, info['output_path'], info['tolerance']))
                    info['last_modified'] = current_mtime
            except FileNotFoundError:
                notify("Warning", f"File not found: {os.path.basename(input_path)}")
        
        # Process changed files
        for input_path, output_path, tolerance in files_to_process:
            file_name = os.path.basename(input_path)
            notify("Starting", f"Processing {file_name}")
            
            try:
                create_depth_map(input_path, output_path, tolerance)
                notify("Finished", f"Successfully processed {file_name}")
            except Exception as e:
                notify("Error", f"Failed to process {file_name}: {e}")
        
        time.sleep(check_interval)

def queue_depth_map(input_path, output_path, tolerance=30):
    """
    Adds a file to the processing queue
    """
    file_queue[input_path].update({
        'last_modified': None,  # Force first processing
        'output_path': output_path,
        'tolerance': tolerance
    })
    notify("Queued", f"Added {os.path.basename(input_path)} to processing queue")

def color_distance(color1, color2):
    return np.sqrt(np.sum((np.array(color1) - np.array(color2)) ** 2))

def combine_images(image_paths, output_path):
    """
    Combine multiple images by taking the maximum value for each color channel.
    All images must be the same size.
    """
    # Open first image to get dimensions
    base_img = Image.open(image_paths[0]).convert('RGB')
    width, height = base_img.size
    
    # Initialize result array with zeros
    result = np.zeros((height, width, 3), dtype=np.uint8)
    
    # Process each image
    for path in image_paths:
        img = Image.open(path).convert('RGB')
        if img.size != (width, height):
            raise ValueError(f"Image {path} has different dimensions than the first image")
        
        # Convert to numpy array
        img_array = np.array(img)
        
        # Update result with maximum values
        result = np.maximum(result, img_array)
    
    # Convert back to image and save
    output_img = Image.fromarray(result)
    output_img.save(output_path)

def create_depth_map(input_path, output_path, tolerance=30):
    # Open image and convert to RGBA to handle transparency
    img = Image.open(input_path).convert('RGBA')
    width, height = img.size
    
    # Convert to numpy array for faster processing
    img_array = np.array(img)
    
    # Create mask for opaque pixels (alpha > 0)
    opaque_mask = img_array[:, :, 3] > 0
    
    # Create mask for transparent pixels (alpha = 0)
    transparent_mask = img_array[:, :, 3] == 0
    
    # Get coordinates of opaque pixels
    opaque_coords = np.column_stack(np.where(opaque_mask))
    
    # Get coordinates of transparent pixels
    transparent_coords = np.column_stack(np.where(transparent_mask))
    
    # Create output array initialized with zeros (black)
    output_array = np.zeros((height, width, 3), dtype=np.uint8)
    
    if len(opaque_coords) > 0 and len(transparent_coords) > 0:
        # Calculate distances from each opaque pixel to all transparent pixels
        distances = cdist(opaque_coords, transparent_coords)
        
        # Find minimum distance for each opaque pixel
        min_distances = np.min(distances, axis=1)
        
        # Clamp distances to 255 (since we can't store values > 255 in uint8)
        distances_clamped = np.minimum(min_distances, 255).astype(np.uint8)
        
        # Set red channel based on distance (0 = black, increasing distance = brighter red)
        for i, (y, x) in enumerate(opaque_coords):
            output_array[y, x, 0] = distances_clamped[i]
    
    # Create and save output image
    output_img = Image.fromarray(output_array)
    output_img.save(output_path)

def monitor_memory():
    """
    Continuously monitors system RAM usage and kills the program if it exceeds threshold
    """
    while True:
        try:
            memory_percent = psutil.virtual_memory().percent
            if memory_percent >= MEMORY_THRESHOLD:
                notify("CRITICAL", f"Memory usage at {memory_percent}%! Emergency shutdown!")
                os._exit(1)  # Force immediate exit
            time.sleep(MEMORY_CHECK_INTERVAL)
        except Exception as e:
            notify("Warning", f"Memory monitor error: {e}")
            time.sleep(MEMORY_CHECK_INTERVAL)

if __name__ == "__main__":
    import threading
    
    # Start the memory monitoring thread
    memory_thread = threading.Thread(target=monitor_memory, daemon=True)
    memory_thread.start()
    
    # Start the file monitoring thread
    monitor_thread = threading.Thread(target=monitor_and_process, daemon=True)
    monitor_thread.start()
    
    # Queue the files for processing
    base_path = "/home/evans/Documents/Java/TowerDefenseProject/art/bugs/"
    
    queue_depth_map(
        base_path + "bugs-bug.png",
        base_path + "bug.png",
        tolerance=50
    )

    queue_depth_map(
        base_path + "bugs-fast bug.png",
        base_path + "fast bug.png",
        tolerance=50
    )

    queue_depth_map(
        base_path + "bugs-broodmother.png",
        base_path + "broodmother.png",
        tolerance=50
    )

    queue_depth_map(
        base_path + "bugs-beetle.png",
        base_path + "beetle.png",
        tolerance=50
    )
    
    try:
        # Keep the main thread alive
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nShutting down...")
