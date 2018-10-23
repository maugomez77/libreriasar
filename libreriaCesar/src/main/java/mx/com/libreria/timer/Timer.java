package mx.com.libreria.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class Timer implements Job{	

 public Timer(){}
 
 public void execute(JobExecutionContext context)throws JobExecutionException {
   try{
	HelloWorld hello_word = new HelloWorld();            
    SayHello(hello_word.sayHello());
   } catch (Exception e) {
    e.printStackTrace();
   }
 }
 public void SayHello(final String msg){
  Date curDate=new Date();
  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  System.out.println("LOG: " + msg + " " + sdf.format(curDate));
 }
}

