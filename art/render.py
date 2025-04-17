import bpy
import os
import math

bpy.data.scenes["Scene"].render.film_transparent = True

def render(name, heading):
    # Set render output path
    output_dir = f"//{name}"  # Relative to blend file location
    if not os.path.exists(bpy.path.abspath(output_dir)):
        os.makedirs(bpy.path.abspath(output_dir))

    # Set render path and format
    bpy.context.scene.render.filepath = f"{output_dir}/{name}-{heading}.png"
    bpy.context.scene.render.image_settings.file_format = 'PNG'

    # Make sure we're using the scene camera
    bpy.context.scene.camera = bpy.context.scene.camera or bpy.data.objects.get('Camera')

    # Render the scene
    bpy.ops.render.render(write_still=True)

    print(f"Rendered image saved to: {bpy.path.abspath(bpy.context.scene.render.filepath)}") 



# d = dot, NESW = North East South West
# split by space for easy making
headings = 'd S E N NS WE W SW NW NE SE NESW'.split()


def size(s):
    bpy.data.scenes["Scene"].render.resolution_x = s
    bpy.data.scenes["Scene"].render.resolution_y = s

def move(x, y):
    bpy.data.objects["Camera"].location[0] = x + .5
    bpy.data.objects["Camera"].location[1] = y + .5

def scale(s):
    size( round(s*60) )
    bpy.data.cameras["Camera"].ortho_scale = s

def ToggleRender(col, shot):
    for each in bpy.data.collections.get(col).objects:
        if "bool" in each.name: continue
        each.hide_render = True if "flash" in each.name else False
        if shot: each.hide_render = not each.hide_render


move(0, 2); scale(1);

bpy.data.materials["Material"].node_tree.nodes["Mix"].inputs[7].default_value = [1.000000, 0.000000, 1.000000, 1.000000]
render( "unknown", "image" )

bpy.data.materials["Material"].node_tree.nodes["Mix"].inputs[7].default_value = [0.000000, 0.000000, 1.000000, 1.000000]
render( "unknown", "tile" )


move(0, 0)

for x in range(12):
    bpy.data.objects["Path"].modifiers["GeometryNodes"]["Socket_2"] = x
    render( "path", headings[x] )

move(0, 4)

for x in range(12):
    bpy.data.objects["Grass"].modifiers["GeometryNodes"]["Socket_2"] = x
    render( "grass", headings[x] )

degreeRanges = range(0, 360, 10)
move(2, 2); scale(1.5);


# simple pew pew
for degree in degreeRanges:
    ToggleRender("Simple Turret", False)
    
    for rot in [0, -19/3 -19/3*2]:
        bpy.data.objects["Plane.003"].rotation_euler[2] = math.radians(degree)
        bpy.data.objects["Plane.008"].rotation_euler[1] = math.radians(rot)
        render( "SimpleTurret", f"{degree}-{rot}" )

    ToggleRender("Simple Turret", True)
    render( "SimpleTurret", f"{degree}-flash" )

    
move(4, 2); scale(2);


# chain pew pew
for degree in degreeRanges:
    ToggleRender("Chain Turret", False)
    
    for rot in [0, -46.3/3 -46.3/3*2]:
        bpy.data.objects["Plane.011"].rotation_euler[2] = math.radians(degree)
        bpy.data.objects["Plane.005"].rotation_euler[1] = math.radians(rot)
        render( "ChainTurret", f"{degree}-{rot}" )

    ToggleRender("Chain Turret", True)
    render( "ChainTurret", f"{degree}-flash" )

    
move(6, 2); scale(2.5);

# sniper
for degree in degreeRanges:
    ToggleRender("Sniper Turret", False)
    
    for recoil in [0]:  # add later maybe
        bpy.data.objects["Plane.013"].rotation_euler[2] = math.radians(degree)
        render( "SniperTurret", f"{degree}-0" )

    ToggleRender("Sniper Turret", True)
    render( "SniperTurret", f"{degree}-flash" )

    
move(8, 2); scale(1);

# sniper
for degree in degreeRanges:
    ToggleRender("Bonk Turret", False)
    
    for recoil in [0]:  # add later maybe
        bpy.data.objects["Circle.003"].rotation_euler[2] = math.radians(degree)
        render( "BonkTurret", f"{degree}-0" )

        







bpy.data.scenes["Scene"].render.film_transparent = False



