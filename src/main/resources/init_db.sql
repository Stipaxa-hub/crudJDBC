CREATE TABLE book (
                      id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255),
                      price DECIMAL(10, 2)
);

CREATE TABLE author (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        last_name VARCHAR(255),
                        first_name VARCHAR(255),
                        age INT,
                        book_id INT
)
