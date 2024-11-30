package vn.com.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {
    private String id;

    private String filePath;

    private String fileName;

    private Long fileSize;

    private String isAvatar; // Y is product avatar, N is list sub image
}
