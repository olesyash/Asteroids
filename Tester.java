import javax.swing.JFrame;


public class Tester
{
	 public static void main(String[] args)
	    {
	        JFrame frame = new JFrame("Tester");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(618, 736);
	        MainPanel mp = new MainPanel();
	        frame.add(mp);
	       	frame.setVisible(true);
	    }
}