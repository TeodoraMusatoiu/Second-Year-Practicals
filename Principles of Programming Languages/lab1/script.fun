rec append(xs, ys) = 
	if xs = nil then ys else head(xs) : append(tail(xs),ys);;

rec concat (xss) = 
	if xss = nil then nil else append (head(xss), concat(tail (xss)));;

val yyy = list(1, list(2, 3), list(4, list(5), 6));;

rec flatten (xss) = 
	if xss = nil then nil else 
		(if integer(head(xss)) then head(xss) : flatten (tail (xss)) else append(flatten(head(xss)), flatten(tail(xss))) );;

rec flatsum (xss) = 
	if xss = nil then 0 else 
		(if integer(head(xss)) then (head(xss) + flatsum (tail (xss))) else (flatsum(head(xss))+ flatsum(tail(xss))) ) ;;

-- >>> flatsum (yyy);;
-- --> 21

val sum(xs) =
	let rec loop(ys, s) =
		if ys = nil then s else loop(tail(ys), s + head(ys)) in
	loop(xs, 0);;

val sumimp(xs) =
	let val ys = new( ) in let val s = new( ) in
	ys := xs; s := 0;
	while !ys <> nil do
		(s := !s + head(!ys); ys := tail(!ys));
	!s;;

val flatsum1 (xss) = 
	let rec loop(yss,s) = 
		if yss = nil then s else 
			(if integer(head(yss)) then loop(tail(yss), s+head(yss)) else loop(append(head(yss), tail(yss)), s)) in
	loop(xss, 0);;

-- >>> flatsum1(yyy);;
-- --> 21

val flatsum2 (xss) =
	let val yss = new( ) in let val s = new( ) in
	yss := xss ; s := 0;
	while !yss <> nil do
		(if integer(head(!yss)) then (s := !s + head(!yss); yss := tail(!yss)) else yss := append( head(!yss), tail(!yss)) );
	!s;;

-- >>> flatsum2 (yyy);;
-- --> 21

-- >>> flatsum2 (xxx);;
-- --> 10