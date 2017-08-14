import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;


public class menu2 {
	 menu2(){  
         Frame f= new Frame("Suji menu");  
         MenuBar mb=new MenuBar();  
         Menu menu=new Menu("Menu");  
         Menu menu2= new Menu("Board");
         Menu submenu=new Menu("Sub Menu");  
         MenuItem i1=new MenuItem("Item 1");  
         MenuItem i2=new MenuItem("Item 2");  
         MenuItem i3=new MenuItem("Item 3");  
         MenuItem i4=new MenuItem("Item 4");  
         MenuItem i5=new MenuItem("Item 5");  
         menu.add(i1);  
         menu.add(i2);  
         menu.add(i3);  
         submenu.add(i4);  
         submenu.add(i5);  
         menu.add(submenu);  
         mb.add(menu);
         mb.add(menu2);
         f.setMenuBar(mb);  
         f.setSize(400,400);  
         f.setLayout(null);  
         f.setVisible(true);  
}  
public static void main(String args[])  
{  
new menu2();  
}

}
