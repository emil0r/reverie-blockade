-- name: sql-is-blocked?

SELECT
        COUNT(*) > 0 AS blocked_p
FROM
        blockade_list
WHERE
        (       domain = :domain
                OR ip = :ip )
        AND active_p = true;
