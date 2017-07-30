{$N+,E-}
{$D-,L-,Y-}
program randDbl;
{ $APPTYPE CONSOLE}
{dcc32 -B -CC -U..\Lib randDbl.pas}

procedure randDoubleSingle(seed:longint);
begin
  randseed := seed;
  writeln(random:17:16,' ',randseed);
end;

procedure randDoubleInterval(start_seed,end_seed:longint);
var i:longint;
begin
  for i:=start_seed to end_seed do begin
    randDoubleSingle(i);
  end;
end;

procedure checkerror(error:integer;argument:string);
begin
if (error <> 0) then begin
  writeln('invalid argument ', argument);
  halt(2);
end;
end;

var start_seed,end_seed:longint;
    error:integer;
begin
if ((paramcount < 1) or (paramcount >2)) then begin
  writeln('Invalid number of arguments.');
  writeln('Usage: randInt seed - generates one double in the interval [0, 1)');
  writeln('Usage: randInt start_seed end_seed - generates one double from each seed');
  halt(1);
end;
if (paramcount = 1) then begin
  val(paramstr(1),start_seed,error);
  checkerror(error,paramstr(1));
  randDoubleSingle(start_seed);
end
else begin
  val(paramstr(1),start_seed,error);
  checkerror(error,paramstr(1));
  val(paramstr(2),end_seed,error);
  checkerror(error,paramstr(2));
  randDoubleInterval(start_seed,end_seed);
end;
end.