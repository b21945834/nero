package tr.com.nero.common;

public enum NeroResponseStatus {
    SUCCESS(1L, "Başarılı", "Success"),
    WARNING(2L, "Dikkat", "Warning"),
    ERROR(3L, "Hata", "Error");

    private final Long id;
    private final String message;
    private final String desc;

    NeroResponseStatus(Long id, String message, String desc){
        this.id = id;
        this.message = message;
        this.desc = desc;
    }
}
