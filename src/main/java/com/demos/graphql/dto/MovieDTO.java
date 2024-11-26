package com.demos.graphql.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

  private Long id;

  private String title;

  private String director;

  private int releaseYear;

}
