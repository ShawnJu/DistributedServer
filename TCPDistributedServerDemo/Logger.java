import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ShangJu on 2/18/15.
 */
public class Logger {
    private Logger(){}
    private PrintWriter output;

    public Logger(String NameOfLog) throws IOException{
        output = new PrintWriter(NameOfLog);
    }

    synchronized void log(String msg) throws  IOException
    {
        StringBuilder logbuilder = new StringBuilder();
        logbuilder.append("[INFO] ");
        logbuilder.append(msg+"\n");
        System.out.print(logbuilder.toString());
        output.write(logbuilder.toString());
        output.flush();
    }

    void closelogger(){
        output.flush();
        output.close();
    }


}
