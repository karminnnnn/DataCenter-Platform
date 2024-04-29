package net.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppToken implements Serializable {
	private Long appId;
	private String appKey;
	private String token;
	private Long expireAt;
}
