{$IFDEF FPC}
  {$mode objfpc} {$H+}
{$ELSE}
  {$N+,E-,D-,L-,Y-}
{$ENDIF}

Program random_integer;
{$IFDEF CONDITIONALEXPRESSIONS}
  {$IF CompilerVersion >= 14}
    {$APPTYPE CONSOLE}
  {$IFEND}
{$ENDIF}
{dcc32 -B -CC -U..\Lib randInt.pas}

Procedure randInteger(seed, range : longint; count : integer);

Var 
  i : integer;
Begin
  randseed := seed;
  For i:=1 To count Do
    Begin
    {$IFDEF FPC}
      writeln(random(range));
    {$ELSE}
      writeln(random(range), ' ', randseed);
    {$ENDIF}
    End;
End;

Procedure checkerror(error : integer; argument : String);
Begin
  If (error <> 0) Then
    Begin
      writeln('invalid argument ', argument);
      halt(2);
    End;
End;

Var 
  seed, range : longint;
  count : integer;
  error : integer;
Begin
  If ((paramcount < 2) Or (paramcount > 3)) Then
    Begin
      writeln('Invalid number of arguments.');
      writeln(
      'Usage: randInt seed range - generates one int in the interval [0, range)'
      );
      writeln(
'Usage: randInt seed range count - generates count ints in the interval [0, range)'
      );
      halt(1);
    End;

  val(paramstr(1), seed, error);
  checkerror(error, paramstr(1));
  val(paramstr(2), range, error);
  checkerror(error, paramstr(2));

  count := 1;
  If (paramcount = 3) Then
    Begin
      val(paramstr(3), count, error);
      checkerror(error, paramstr(3));
    End;
  randInteger(seed, range, count);
End.
