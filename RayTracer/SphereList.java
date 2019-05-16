public class SphereList{

Sphere first;
Sphere iter;

  public SphereList(){

    first = null;
    iter = first;
  }

  public void add(Sphere newSphere){

    newSphere.next = first;
    first = newSphere;
  }

  public void startIterator(){

    iter = first;
  }

  public boolean hasNext(){

    if(iter == null)
      return false;
    else
      return true;
  }
  public Sphere next(){

    Sphere keep = iter;
    iter = iter.next;
    return keep;
  }
}
