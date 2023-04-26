package exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler
{
    @org.springframework.web.bind.annotation.ExceptionHandler(
            {
                    UnknownHostException.class,
                    SocketTimeoutException.class
            }
    )
    public void handleException(Exception e)
    {
        System.out.println(e.getMessage());
    }
}
