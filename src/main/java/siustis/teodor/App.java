package siustis.teodor;

import java.io.File;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.google.common.collect.Table;

public class App extends JFrame
{
	static File file;
    public static void main( String[] args ) throws Exception
    {
			new GUI().setVisible(true);

    }
}
    
    
