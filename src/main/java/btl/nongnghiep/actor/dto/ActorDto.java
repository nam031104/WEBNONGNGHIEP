package btl.nongnghiep.actor.dto;


public class ActorDto {
    private String idActor;
    private String typeActor;
    private int mode;
    private int status;

    public String getIdActor() {
        return idActor;
    }

    public void setIdActor(String idActor) {
        this.idActor = idActor;
    }

    public String getTypeActor() {
        return typeActor;
    }

    public void setTypeActor(String typeActor) {
        this.typeActor = typeActor;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
