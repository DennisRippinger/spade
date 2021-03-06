/**
 *
 */
package info.interactivesystems.spade.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Dennis Rippinger
 */
@Getter
@Setter
@Entity
@Table(name = "stylometry")
public class Stylometry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	private Double density;

	// Readability

	private Double ari;

	private Double gfi;

	// Lexical

	private Double wordLevel;

	private Double digits;

	private Double digitBigrams;

	private Double digitTrigrams;

	private Double dollarOccurence;

	private Double commaOccurence;

	// Syntactic

	private Double functionWordFrequency;

	private Double exclamationMarkOccurence;

	private Double questionmarkOccurence;

}
