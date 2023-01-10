SHELL = /bin/bash

morfflex-xz = czech-morfflex-2.0.tsv.xz
morfflex = czech-morfflex-2.0.tsv

book = book.txt

$(morfflex-xz):
	# Download morfflex 2.0 from the official repository
	echo "Downloading MorfFlex from the official repository"
	echo "This may take a while. Please, wait."
	curl -o $(morfflex-xz) --remote-name-all https://lindat.mff.cuni.cz/repository/xmlui/bitstream/handle/11234/1-3186{/czech-morfflex-2.0.tsv.xz}
	echo "Done."

$(morfflex): $(morfflex-xz)
	echo "Decompressing the MorfFlex file"
	echo "This may take a while. Please, wait."
	unxz --keep $(morfflex-xz)
	echo "Done."

$(book):
	echo "Downloading an example book in Czech, that can be checked and corrected by the program."
	echo "It will be stored in the file:"
	echo $(book)
	wget -O $(book)	https://www.gutenberg.org/files/34225/34225-0.txt

myclear:
	bash clear.sh

recompile: myclear
	echo "Compiling the java files."
	javac cz/cuni/mff/souradat/spellcheck/Main.java
	echo "Done."

run: recompile $(morfflex) $(book)
	echo "Running the main shell of the program..."
	java -Xmx11g cz.cuni.mff.souradat.spellcheck.Main

clean:
	rm $(book)
	rm $(morfflex)
	rm $(morfflex-xz)
