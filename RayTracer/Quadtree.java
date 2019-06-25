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
ArrayList<Sphere> spheres;

  public Quadtree(double xMinimum, double xMaximum, double yMinimum, double yMaximum, int nodeDepth){

    //Initialize variables.
    xMin = xMinimum;
    xMax = xMaximum;
    yMin = yMinimum;
    yMax = yMaximum;
    depth = nodeDepth;
    counter = 0;
    spheres = new ArrayList<Sphere>();
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

    spheres.add(theSphere);
    counter++;
  }

  //Function: insertSphere.
  //Recursively traverses the tree and checks if a sphere should be inserted into a node.
  public void insertSphere(Sphere aSphere){

    //Check if the current node is a leaf.
    if(depth == 3){
      //If it is, see if the bounding box of the sphere overlaps the box for the leaf.
      if(((aSphere.boxXMax >= xMin && xMin >= aSphere.boxXMin) ||
      (aSphere.boxXMax >= xMax && xMax >= aSphere.boxXMin) ||
      (xMax >= aSphere.boxXMin && aSphere.boxXMin >= xMin) ||
      (xMax >= aSphere.boxXMax && aSphere.boxXMax >= xMin)) && (
      (aSphere.boxYMax >= yMin && yMin >= aSphere.boxYMin) ||
      (aSphere.boxYMax >= yMax && yMax >= aSphere.boxYMin) ||
      (yMax >= aSphere.boxYMin && aSphere.boxYMin >= yMin) ||
      (yMax >= aSphere.boxYMax && aSphere.boxYMax >= yMin))){
        //If the sphere passes, insert it into the leaf.
        addSphere(aSphere);
      }
    }
    //If node is not a leaf, check each child.
    else{
      if(((aSphere.boxXMax >= ul.xMin && ul.xMin >= aSphere.boxXMin) ||
      (aSphere.boxXMax >= ul.xMax && ul.xMax >= aSphere.boxXMin) ||
      (ul.xMax >= aSphere.boxXMin && aSphere.boxXMin >= ul.xMin) ||
      (ul.xMax >= aSphere.boxXMax && aSphere.boxXMax >= ul.xMin)) && (
      (aSphere.boxYMax >= ul.yMin && ul.yMin >= aSphere.boxYMin) ||
      (aSphere.boxYMax >= ul.yMax && ul.yMax >= aSphere.boxYMin) ||
      (ul.yMax >= aSphere.boxYMin && aSphere.boxYMin >= ul.yMin) ||
      (ul.yMax >= aSphere.boxYMax && aSphere.boxYMax >= ul.yMin))){
        ul.insertSphere(aSphere);
      }
      if(((aSphere.boxXMax >= ur.xMin && ur.xMin >= aSphere.boxXMin) ||
      (aSphere.boxXMax >= ur.xMax && ur.xMax >= aSphere.boxXMin) ||
      (ur.xMax >= aSphere.boxXMin && aSphere.boxXMin >= ur.xMin) ||
      (ur.xMax >= aSphere.boxXMax && aSphere.boxXMax >= ur.xMin)) && (
      (aSphere.boxYMax >= ur.yMin && ur.yMin >= aSphere.boxYMin) ||
      (aSphere.boxYMax >= ur.yMax && ur.yMax >= aSphere.boxYMin) ||
      (ur.yMax >= aSphere.boxYMin && aSphere.boxYMin >= ur.yMin) ||
      (ur.yMax >= aSphere.boxYMax && aSphere.boxYMax >= ur.yMin))){
        ur.insertSphere(aSphere);
      }
      if(((aSphere.boxXMax >= ll.xMin && ll.xMin >= aSphere.boxXMin) ||
      (aSphere.boxXMax >= ll.xMax && ll.xMax >= aSphere.boxXMin) ||
      (ll.xMax >= aSphere.boxXMin && aSphere.boxXMin >= ll.xMin) ||
      (ll.xMax >= aSphere.boxXMax && aSphere.boxXMax >= ll.xMin)) && (
      (aSphere.boxYMax >= ll.yMin && ll.yMin >= aSphere.boxYMin) ||
      (aSphere.boxYMax >= ll.yMax && ll.yMax >= aSphere.boxYMin) ||
      (ll.yMax >= aSphere.boxYMin && aSphere.boxYMin >= ll.yMin) ||
      (ll.yMax >= aSphere.boxYMax && aSphere.boxYMax >= ll.yMin))){
        ll.insertSphere(aSphere);
      }
      if(((aSphere.boxXMax >= lr.xMin && lr.xMin >= aSphere.boxXMin) ||
      (aSphere.boxXMax >= lr.xMax && lr.xMax >= aSphere.boxXMin) ||
      (lr.xMax >= aSphere.boxXMin && aSphere.boxXMin >= lr.xMin) ||
      (lr.xMax >= aSphere.boxXMax && aSphere.boxXMax >= lr.xMin)) && (
      (aSphere.boxYMax >= lr.yMin && lr.yMin >= aSphere.boxYMin) ||
      (aSphere.boxYMax >= lr.yMax && lr.yMax >= aSphere.boxYMin) ||
      (lr.yMax >= aSphere.boxYMin && aSphere.boxYMin >= lr.yMin) ||
      (lr.yMax >= aSphere.boxYMax && aSphere.boxYMax >= lr.yMin))){
        lr.insertSphere(aSphere);
      }
    }
  }

  //Function: getList
  //Searches the tree for the leaf which envelopes a set of coordinates and return its spheres.
  public Sphere[] getList(double xPosition, double yPosition){

    //If the current node is a leaf, return its spheres.
    if(depth == 3)
      return spheres.toArray(new Sphere[counter]);
    //Otherwise, find out which child the coordinates belong to and try there.
    else if((xPosition >= ul.xMin && xPosition <= ul.xMax) &&
    (yPosition >= ul.yMin && yPosition <= ul.yMax))
      return ul.getList(xPosition, yPosition);
    else if((xPosition >= ur.xMin && xPosition <= ur.xMax) &&
    (yPosition >= ur.yMin && yPosition <= ur.yMax))
      return ur.getList(xPosition, yPosition);
    else if((xPosition >= ll.xMin && xPosition <= ll.xMax) &&
    (yPosition >= ll.yMin && yPosition <= ll.yMax))
      return ll.getList(xPosition, yPosition);
    else
      return lr.getList(xPosition, yPosition);
  }
}
