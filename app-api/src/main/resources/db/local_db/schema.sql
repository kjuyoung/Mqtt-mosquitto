CREATE TABLE IF NOT EXISTS vehicle_info (
  id bigint NOT NULL AUTO_INCREMENT,
  driver_id varchar(255),
  vehicle_name varchar(255),
  vehicle_id varchar(255),
  air_press varchar(255),
  temp varchar(255),
  gps varchar(255),
  PRIMARY KEY (id)
) ENGINE=InnoDB;