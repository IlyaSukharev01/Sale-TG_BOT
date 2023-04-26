package exception;

public class UnknownHostException extends java.net.UnknownHostException
{
    @Override
    public String getMessage()
    {
        return "Потеряно интернет соединение";
    }
}
