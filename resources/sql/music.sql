-- :name sql-get-albums :? :*
select 
  album.id        album_id,
  album.name      album_name,
  artist.name     artist_name,
  album.released  album_released 
from 
  albums album
  join artists artist on album.artist = artist.id
where
  album.iname like '%' || :album_name || '%'
order by
  album.name asc
limit :limit

-- :name sql-get-album-by-id :? :1
select 
  album.id        album_id,
  album.name      album_name,
  artist.name     artist_name,
  album.released  album_released
from 
  albums album
  join artists artist on album.artist = artist.id
where
  album.id = :album_id


-- :name sql-get-album-tracks :? :*
select 
  track.id        track_id,
  track.name      track_name,
  track.position  track_position,
  track.length    track_length
from 
  tracks track
where
  track.album = :album_id
order by
  track.position asc


-- :name sql-get-artists :? :*
select 
  artist.id             artist_id,
  artist.name           artist_name,
  artist.disambiguation artist_disambiguation,
  count(album.id)       artist_albums_count
from 
  artists artist
  join albums album on album.artist = artist.id
where
  artist.iname like '%' || :artist_name || '%'
group by
  artist.id
order by
  artist.name asc
limit :limit


-- :name sql-get-artist-by-id :? :1
select 
  artist.id             artist_id,
  artist.name           artist_name,
  artist.disambiguation disambiguation,
  count(album.id)       albums
from 
  artists artist
  join albums album on album.artist = artist.id
where
  artist.id = :artist_id
group by
  artist.id
