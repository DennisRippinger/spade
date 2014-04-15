package info.interactivesystems.spade.util;

/**
 * Temporary concurrent bit
 * 
 * @author Dennis Rippinger
 * 
 */
public enum ConcurrentBit {
    
    UNPROCESSED(0),
    IN_WORK(1),
    PROSSED(2),
    MARKED(3);

    private Integer id;

    private ConcurrentBit(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
