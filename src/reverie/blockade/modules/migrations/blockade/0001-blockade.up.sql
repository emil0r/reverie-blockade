CREATE TABLE blockade_list (
       id serial primary key not null,
       title text not null,
       domain text null,
       ip text null,
       active_p boolean not null default false
);

CREATE TABLE blockade_stats (
       id serial primary key not null,
       domain text null,
       ip text null,
       useragent text null,
       created timestamp not null default now()
);

CREATE INDEX index_blockade_stats_domain ON blockade_stats(domain);
CREATE INDEX index_blockade_stats_ip ON blockade_stats(ip);
