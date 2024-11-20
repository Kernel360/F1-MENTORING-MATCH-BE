ALTER TABLE `content`
    ADD COLUMN `content_level` ENUM ('LOW', 'MEDIUM', 'HIGH') DEFAULT NULL;