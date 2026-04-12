# Postgresql queries

## Players results in the 2023-2024 season by practitioner name like 'SANSO'
```code
select 
	m.match_date,
	m.competition_category,
	m.competition_scope,
	cl."name",
	pl.full_name,
	sprl.match_games_won,
	cv."name",
	pv.full_name,
	sprv.match_games_won
from
	public.players_single_match m
	inner join public.season_player_result sprl on (m.player_result_local_id = sprl.id)
		inner join public.season_player spl on (sprl.season_player_id=spl.id)
			inner join public.club_member cml on (spl.club_member_id=cml.id)
				inner join public.practicioner pl on (cml.practicioner_id=pl.id)
				inner join public.club cl on (cml.club_id=cl.id)
	inner join public.season_player_result sprv on (m.player_result_visitor_id = sprv.id)
		inner join public.season_player spv on (sprv.season_player_id=spv.id)
			inner join public.club_member cmv on (spv.club_member_id=cmv.id)
				inner join public.practicioner pv on (cmv.practicioner_id=pv.id)
				inner join public.club cv on (cmv.club_id=cv.id)
where
	m.season = '2023-2024'
	--and m.competition_category='primera-nacional'
	--and m.competition_gender='male'
	--and m.competition_group='1'
	--and m.competition_scope='nacional'
	--and m.competition_scopetag='esp'
	--and m.competition_type='senior'
	and (pv.full_name like '%SANSO%' or pl.full_name like '%SANSO%')
```