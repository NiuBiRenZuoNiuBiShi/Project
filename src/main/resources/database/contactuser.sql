CREATE TABLE Contacts (
    contact_id BINARY(16) NOT NULL PRIMARY KEY,     
    user_id BINARY(16) NOT NULL,                 
    contact_name VARCHAR(100) NOT NULL,             
    contact_phone VARCHAR(20),                       
    contact_email VARCHAR(100),                      
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    del TINYINT(1) DEFAULT 0                         
) COMMENT '联系人表';
