


TARGETS=\
	dist/gammusca.zip


dist/gammusca.zip:
	make clean
	mkdir -p dist
	sbt onedir
	rm -rf /tmp/gammusca
#	mkdir -p /tmp/gammusca/bin
	rsync -avzrhP --exclude=cache --exclude=.history --exclude=streams \
		--exclude=resolution-cache gammusca/target/ /tmp/gammusca
#	cp etc/run.sh /tmp/gammusca/bin/run
#	chmod +x /tmp/gammusca/bin/run
	chmod +x /tmp/gammusca/start.sh
	cd /tmp && \
		rm -f gammusca.zip && \
		zip -r gammusca.zip gammusca
	mv /tmp/gammusca.zip $@


clean:
	rm -rf dist/*
	sbt clean
	rm -rf target

.PHONY: dist/gammusca.zip

