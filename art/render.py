import bpy
import os

bpy.data.scenes["Scene"].render.film_transparent = True

def render(heading):
    # Set render output path
    output_dir = "//path/"  # Relative to blend file location
    if not os.path.exists(bpy.path.abspath(output_dir)):
        os.makedirs(bpy.path.abspath(output_dir))

    # Set render path and format
    bpy.context.scene.render.filepath = f"{output_dir}path-{heading}.png"
    bpy.context.scene.render.image_settings.file_format = 'PNG'

    # Make sure we're using the scene camera
    bpy.context.scene.camera = bpy.context.scene.camera or bpy.data.objects.get('Camera')

    # Render the scene
    bpy.ops.render.render(write_still=True)

    print(f"Rendered image saved to: {bpy.path.abspath(bpy.context.scene.render.filepath)}") 


# Get the plane object and its geometry nodes modifier
object = bpy.data.objects["Plane"]
modifier = object.modifiers["GeometryNodes.001"]


# d = dot, NESW = North East South West
# split by space for easy making
headings = 'd S E N NS WE W SW NW NE SE NESW'.split()

for x in range(12):
    bpy.data.objects["Plane"].modifiers["GeometryNodes.001"]["Socket_2"] = x
    render( headings[x] )

bpy.data.scenes["Scene"].render.film_transparent = False