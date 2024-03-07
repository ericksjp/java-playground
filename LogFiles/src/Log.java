import java.time.LocalDateTime;

public class Log {
    private final String userName;
    private final LocalDateTime logTime;

    public Log(String userName, LocalDateTime logTime) {
        this.userName = userName;
        this.logTime = logTime;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Log other = (Log) obj;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

}

