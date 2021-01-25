package org.step.linked.step.dto.response;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;

//@JsonRootName("user_update_response")
@JsonPropertyOrder(value = {"username", "id"})
//@JsonAutoDetect()
//@JsonInclude(content = JsonInclude.Include.NON_EMPTY)
//@JsonIgnoreProperties(value = {"id"})
public class UserUpdateResponse {

//    @JsonIgnore
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    public UserUpdateResponse() {
    }

    @JsonCreator
    public UserUpdateResponse(
            @JsonProperty("id") String id,
            @JsonProperty("username") String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
