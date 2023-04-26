package exception;

public class SocketTimeoutException extends java.net.SocketTimeoutException
{
    @Override
    public String getMessage()
    {
        return "Превышено время ожидания.";
    }
}
