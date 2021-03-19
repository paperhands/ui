let
   pkgs = import <nixpkgs> {};
in pkgs.stdenv.mkDerivation rec {
  name = "pepehands";
  buildInputs = with pkgs; [
    nodePackages.live-server
    nodePackages.yarn
  ];
   LD_LIBRARY_PATH = "${pkgs.stdenv.lib.makeLibraryPath buildInputs}";
}
