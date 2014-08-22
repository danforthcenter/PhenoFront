USE `PhenoFront`;

-- --------------------------------------------------------

--
-- Add list of downloaded snapshots to query metadata
--
ALTER TABLE query_metadata ADD COLUMN `missed_snapshots` TEXT;