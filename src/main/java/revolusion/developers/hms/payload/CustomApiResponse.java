package revolusion.developers.hms.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response wrapper for API responses")
public class CustomApiResponse<T> {


    @Schema(description = "Message of the response")
    private String message;

    @Schema(description = "Indicates if the operation was successful")
    private boolean success;

    private T data;
}
