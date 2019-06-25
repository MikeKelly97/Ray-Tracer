
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class RayTracer{

  public static void main(String[] args) throws Exception{

    if(args.length != 1){
      System.out.println("Usage: java RayTracer filename");
      System.exit(0);
    }
    //Make a world object.
    World theWorld = makeWorld(new File(args[0]));
    //Draw the world object.
    Example01 picture = new Example01(theWorld);
  }

  //Function: getColor.
  //Takes a pair of pixel coordinates and returns the color for that pixel.
  public static Color getColor(int x, int y, World w){

    //Initialize variables.
    Vector finalPosition = new Vector(0, 0, Integer.MIN_VALUE);
    Sphere closeSphere = new Sphere(0, 0, 0, 0, new Color((float)0.0, (float)0.0, (float)0.0));
    Color theColor;
    //Coordinitize the pixel.
    double planeX = 10.0 * (((2.0 * x) / 512.0) - 1.0);
    double planeY = -10.0 * (((2.0 * y) / 512.0) - 1.0);
    //Get the magnitude of the vector between the camera and the point on the image plane.
    double mag = Math.pow(Math.pow(planeX, 2) + Math.pow(planeY, 2) + Math.pow(w.camera, 2), 0.5);
    //Make a ray from the camera to the point.
    Ray theRay = new Ray(new Vector(planeX/mag , planeY/mag, (-1 * w.camera)/mag), w.camera);
    //Get the spheres that ray may intersect.
    Sphere[] list = w.sphereTree.getList(planeX, planeY);
    boolean isHit = false;
    //For each sphere the ray may hit.
		for(Sphere currentSphere: list){
      //Get the position hit.
      Vector shiftPoint = new Vector(0 - currentSphere.xCoord, 0 - currentSphere.yCoord,
                                     w.camera - currentSphere.zCoord);
      Vector positionHit = getPositionHit(theRay, currentSphere, shiftPoint);
      //Check that it hit at all.
      if(positionHit != null){
        isHit = true;
        //If it hit, see if it is the closest point hit.
        if(positionHit.zComponent > finalPosition.zComponent){
          finalPosition = positionHit;
          closeSphere = currentSphere;
        }
      }
    }
    //If the ray did not hit any spheres, return background color.
    if(isHit == false)
      theColor = new Color((float)1.0, (float)1.0, (float)0.0);
    //Otherwise, return the shading at the closest point hit.
    else
      theColor = shading(w.light, finalPosition, closeSphere);
    return theColor;
  }

  //Function: makeWorld.
  //Reads an input file and makes a world object with all necessary data.
  public static World makeWorld(File worldFile) throws Exception{

    //Create a Quadtree with four levels.
    Quadtree worldTree = new Quadtree(-10, 10, -10, 10, 0);
    buildSphereTree(worldTree);
    World currentWorld = new World();
    //Read the input file.
    Scanner worldScan = new Scanner(worldFile);
    //Get the camera position.
    String camLine = worldScan.nextLine();
    Scanner camScan = new Scanner(camLine);
    camScan.next();
    currentWorld.camera = camScan.nextDouble();
    //Get the light vector.
    String lightLine = worldScan.nextLine();
    Scanner lightScan = new Scanner(lightLine);
    lightScan.next();
    double x = lightScan.nextDouble();
    double y = lightScan.nextDouble();
    double z = lightScan.nextDouble();
    currentWorld.light = new Vector(x, y, z);
    //Read all spheres and create sphere objects from them.
    while(worldScan.hasNext()){
      String sphereLine = worldScan.nextLine();
      Sphere currentSphere = makeSphere(sphereLine, currentWorld.camera);
      //Insert spheres into the tree.
      worldTree.insertSphere(currentSphere);
    }
    currentWorld.sphereTree = worldTree;
    return currentWorld;
  }

  //Function: buildSphereTree.
  //Recursively expands a Quadtree until it has 4 levels.
  public static void buildSphereTree(Quadtree currentTree){

    if(currentTree.depth == 3)
      return;
    else{
      currentTree.expand();
      buildSphereTree(currentTree.ul);
      buildSphereTree(currentTree.ur);
      buildSphereTree(currentTree.ll);
      buildSphereTree(currentTree.lr);
    }
  }

  //Function: makeSphere.
  //Takes a line of input and makes a sphere object to represent it.
  public static Sphere makeSphere(String sphereText, double camPosition){

    //Read the line.
    Scanner sphereScan = new Scanner(sphereText);
    sphereScan.next();
    //Get the coordinates of the center.
    double x = sphereScan.nextDouble();
    double y = sphereScan.nextDouble();
    double z = sphereScan.nextDouble();
    //Get the radius.
    double radius = sphereScan.nextDouble();
    //Get the color.
    float red = sphereScan.nextFloat();
    float green = sphereScan.nextFloat();
    float blue = sphereScan.nextFloat();
    //Calculate bounding box.
    double yTheta = Math.asin(radius / Math.pow(Math.pow(y, 2) + Math.pow(z - camPosition, 2) , 0.5));
    double yPhi = Math.asin(y / Math.pow(Math.pow(y, 2) + Math.pow(z - camPosition, 2) , 0.5)) - yTheta;
    double xTheta = Math.asin(radius / Math.pow(Math.pow(x, 2) + Math.pow(z - camPosition, 2) , 0.5));
    double xPhi = Math.asin(x / Math.pow(Math.pow(x, 2) + Math.pow(z - camPosition, 2) , 0.5)) - xTheta;
    //Make the sphere.
    Sphere theSphere = new Sphere(radius, x, y, z, new Color(red, green, blue));
    theSphere.boxYMin = camPosition * Math.tan(yPhi);
    theSphere.boxYMax = camPosition * Math.tan(yPhi + (2 * yTheta));
    theSphere.boxXMin = camPosition * Math.tan(xPhi);
    theSphere.boxXMax = camPosition * Math.tan(xPhi + (2 * xTheta));
    return theSphere;
  }

  //Function: getPositionHit.
  //Takes a ray, a sphere, and a shifted camera position, and finds where the ray intersects the sphere.
  public static Vector getPositionHit(Ray aRay, Sphere aSphere, Vector aPosition){

    double smallT = 0.0;
    //Calculate b.
    double b = (2 * aPosition.xComponent * aRay.direction.xComponent) +
               (2 * aPosition.yComponent * aRay.direction.yComponent) +
               (2 * aPosition.zComponent * aRay.direction.zComponent);
    //Calculate c.
    double c = (Math.pow(aPosition.xComponent, 2) + Math.pow(aPosition.yComponent, 2)
                + Math.pow(aPosition.zComponent, 2)) - Math.pow(aSphere.radius, 2);
    //Find two solutions to the quadratic forumula.
    double t1 = ((-1 * b) + Math.pow(Math.pow(b, 2) - (4 * c), 0.5))/2;
    double t2 = ((-1 * b) - Math.pow(Math.pow(b, 2) - (4 * c), 0.5))/2;
    //If NaN is the result, return null as the ray did not hit.
    if(!(t1 > Integer.MIN_VALUE))
        return null;
    //Otherwise, find the smallest linear combination of the ray vector which intersects the sphere.
    else if(t1 >= t2)
      smallT = t2;
    else
      smallT = t1;
    //Find the position hit using that linear combination.
    return new Vector(smallT * aRay.direction.xComponent, smallT * aRay.direction.yComponent,
                      smallT * aRay.direction.zComponent + aRay.camera);
  }

  //Function: shading.
  //Takes the position hit, the sphere and the lighting and returns the shaded color.
  public static Color shading(Vector lighting, Vector spherePosition, Sphere theSphere){

    //Get the components of the surface normal.
		double nX = spherePosition.xComponent - theSphere.xCoord;
		double nY = spherePosition.yComponent - theSphere.yCoord;
		double nZ = spherePosition.zComponent - theSphere.zCoord;
    //Get the magnitude of the un-normalized surface normal.
		double mag = Math.pow(Math.pow(nX, 2) + Math.pow(nY, 2) + Math.pow(nZ, 2), 0.5);
    //Make the surface normal.
		Vector n = new Vector(nX/mag, nY/mag, nZ/mag);
    //Calculate the color multiplier.
		double multiplier = ((nX/mag) * lighting.xComponent) + ((nY/mag) * lighting.yComponent) + ((nZ/mag) * lighting.zComponent);
		if(multiplier < 0)
			multiplier = 0.0;
    //Return the shaded color.
		return (new Color((int)(multiplier * theSphere.theColor.getRed()), (int)(multiplier * theSphere.theColor.getGreen()), (int)(multiplier * theSphere.theColor.getBlue())));
  }
}
