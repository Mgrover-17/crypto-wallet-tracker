//package com.cg.crypto_wallet.DTO;
//
//import com.cg.crypto_wallet.model.User;
//import jakarta.persistence.Column;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//
//@Data
//@Validated
//@NoArgsConstructor
//public class UserDto {
//    @Pattern(
//            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
//            message = "Invalid email format"
//    )
//    @Email(message = "Email should be valid")
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
//    @Column(nullable = false)
//    private String password;
//
//    @NotBlank(message = "Username is required")
//    @Size(min = 3, max = 20, message = "Fullname must be between 3 to 20 characters")
//    @Column(nullable = false)
//    private String fullname;
//
//    @NotBlank(message = "Username is required")
//    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
//    @Column(nullable = false)
//    private String name;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
//    @Column(nullable = false)
//    private String confirmPassword;
//
//    private String otp;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, message = "Password must be at least 6 characters long")
//    @Column(nullable = false)
//    private String newPassword;
//
//    public UserDto(String email, String password){
//         this.email = email;
//         this.password = password;
//    }
//
//    public UserDto(String fullname,String email,String password){
//         this.fullname = fullname;
//         this.email = email;
//         this.password = password;
//    }
//
//    public UserDto(String email,String newPassword , String confirmPassword, String otp){
//        this.email = email;
//        this.newPassword = newPassword;
//        this.confirmPassword = confirmPassword;
//        this.otp = otp;
//    }
//
//}
