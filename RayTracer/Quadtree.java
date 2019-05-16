import java.util.*;

public class Quadtree{

//Declare variables.
double xMin;
double xMax;
double yMin;
double yMax;
int depth;
int counter;
Quadtree ul;
Quadtree ur;
Quadtree ll;
Quadtree lr;
Sphere[] spheres;

  public Quadtree(double xMinimum, double xMaximum, double yMinimum, double yMaximum, int nodeDepth){

    //Initialize variables.
    xMin = xMinimum;
    xMax = xMaximum;
    yMin = yMinimum;
    yMax = yMaximum;
    depth = nodeDepth;
    counter = 0;
    spheres = new Sphere[0];
    ul = null;
    ur = null;
    ll = null;
    lr = null;
  }

  //Method: expand.
  //Makes four children which each have 1/4 the bounding box.
  public void expand(){

    ul = new Quadtree(xMin, (xMax + xMin)/2, (yMax + yMin)/2, yMax, depth + 1);
    ur = new Quadtree((xMax + xMin)/2, xMax, (yMax + yMin)/2, yMax, depth + 1);
    ll = new Quadtree(xMin, (xMax + xMin)/2, yMin, (yMax + yMin)/2, depth + 1);
    lr = new Quadtree((xMax + xMin)/2, xMax, yMin, (yMax + yMin)/2, depth + 1);
  }

  //Method: addSphere.
  //Adds a sphere to the node's array.
  public void addSphere(Sphere theSphere){

    spheres = Arrays.copyOf(spheres, spheres.length + 1);
    spheres[counter] = theSphere;
    counter++;
  }
}
