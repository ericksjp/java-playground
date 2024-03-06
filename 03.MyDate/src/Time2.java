public class Time2 {
    private int hora;
    private int minuto;
    private int segundo;

    public Time2(){
        this(0,0,0);
    }

    public Time2(int hora) {
        this(hora,0,0);
    }

    public Time2(int hora, int minuto){
        this(hora,minuto,0);
    }

    public Time2(int hora, int minuto, int segundo){
        if (hora > 23 || hora < 0){
            throw new IllegalArgumentException("hour must be 0-23");
        }
        if (minuto > 59 || minuto < 0){
            throw new IllegalArgumentException("minute must be 0-59");
        }
        if (segundo > 59 || segundo < 0){
            throw new IllegalArgumentException("second must be 0-59");
        }
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo; 
    }

    public void setTime(int hora, int minute, int second){
        if (hora > 23 || hora < 0){
            throw new IllegalArgumentException("hour must be 0-23");
        }
        if (minuto > 59 || minuto < 0){
            throw new IllegalArgumentException("minute must be 0-59");
        }
        if (segundo > 59 || segundo < 0){
            throw new IllegalArgumentException("second must be 0-59");
        }
        this.hora = hora;
        this.minuto = minute;
        this.segundo = second;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        if (hora > 23 || hora < 0){
            throw new IllegalArgumentException("hour must be 0-59");
        }
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        if (minuto > 59 || minuto < 0){
            throw new IllegalArgumentException("minute must be 0-59");
        }
        this.minuto = minuto;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        if (segundo > 59 || segundo < 0){
            throw new IllegalArgumentException("second must be 0-59");
        }
        this.segundo = segundo;
    }

    public String toUniversalString(){
        return String.format("%02d:%02d:%02d", getHora(), getMinuto(), getSegundo());
    }

    public String toString(){
        return String.format("%02d:%02d:%02d %s",
                (getHora() == 0 || getHora() == 12) ? 12 : getHora() % 12, minuto, segundo,
                getHora() > 12 ? "PM":"AM");
    }

}
