package info.interactivesystems.spade.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * DTO for a UncategorizedReview.
 * 
 * @author Dennis Rippinger
 * 
 */
@Data
@Entity
@Table(name = "Users")
public class User {

	@Id
	private String id;

	private String name;

	private Integer ranking;

	private Integer helpfulness;

	private Integer helpfulVotes;

	private Integer helpfulOverallVotes;

	private Integer numberOfReviews;

}
