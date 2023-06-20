package com.rnd.baseproject.retrofit_api.response

data class AuthResponse(
    var success: Boolean,
    var message: String,
    var status: Int,
    var data: User
)


/**
 * {
"success": true,
"message": "User register successfully",
"status": 1,
"data": {
"id": 6,
"first_name": "",
"last_name": "",
"phone_number": "9924303020",
"email": "611063@gmail.com",
"profile_image": "",
"country_id":"91",
"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiY2UwOWRkN2Y5MWJkYzlhYTc3YWQ4YzczMDAxNDBlMmVlYWY1MjkzZDBiMDQ2NmU1MGUxNjUyYjVhY2Y4ZGM1YzI2MzNlZjZmZmFmOWUxNmMiLCJpYXQiOjE2ODExOTQwMTUuNDkyNzkzLCJuYmYiOjE2ODExOTQwMTUuNDkyODAxLCJleHAiOjE3MTI4MTY0MTUuNDc1MDY1LCJzdWIiOiI2Iiwic2NvcGVzIjpbXX0.qD9PgRjmf2VxZB9GbY1tvEeqGn3OvWFo0qAGgO4ezuvdwKjwb2_pgaVxVAXvZExPhgiRTUEuxkC1aYQMgVVBN908ySjCp4aZiP5hqWLSB2rQWw4CBJN06plowm6VnOyINXW-1KoBhuJVZqhjC4eaW1Risj81UXeSpe_7ja4vf1MsUhhYTXfasd2RwG6hPzp-RwLEWUeZj38PcY_FSOwWkmXHDoZOXwTlheNnKearBMcVB-E6oDNZYvwR-6ioREZg3-r97icOzLiLwNAJas4pDre4U56MLGW-gl3A7XuDC6v3PdAGSpMI9NzcVZFxsq_lF2xX52BDBaCw1G6DXA7_Ftwuz21s-mt78s6k8UiLNorzOeibleXEO2qcaRyy5SdQlUTk7eeTzqYWmsW7LDJLMxUvzdgyLzX9Wo-tXOZvyKvJjS1io190yJ9tvBkHbMahFLhQ-B2CdAzmHJnK9c8PcK_MJCgs5NPHBYLZp-Q3OHAASxXdfdH9eyhLiLLs2WOo_SfhikPmtqnLaw3OBMo4UxCTjGxgjx6rFrKUILDh7DViDOg_9dIV16OUREgy8ZGmVWZairmbWn5P3POFYsXK951f4S9MEoX_kPN08NnARM-Bjjk_g2Jq_0R2adJRqD6uM2Pw-ZHnE1iGvgMLACzD2RImcWT1bxfdK9sjsChd620",
"associative_id": 0,
"is_phone_verified": 0,
"is_email_verified": 0,
"email_verified_at": null,
"phone_number_verified_at": null,
"is_gps": 0,
"is_active": 1,
"is_deleted": 0,
"is_blocked": 0,
"is_registered": 0,
"is_account_verified": 0,
"has_subscription": 1,
"created_by": 0,
"updated_by": 0,
"deleted_by": 0,
"created_on": 1681194015,
"updated_on": 1681194015,
"deleted_on": 0,
"otp": 611063
}
}*/
