


TARGETS=\
	dist/gammusca.zip


dist/gammusca.zip:
	sbt proguard
	rm -rf /tmp/gammusca
	mkdir -p /tmp/gammusca/bin
	cp target/class/gammusca-$(VERSION).min.jar /tmp/gammusca/
	cp etc/run.sh /tmp/gammusca/bin/run
	chmod +x /tmp/gammusca/bin/run
	cd /tmp && \
		rm -f gammusca.zip && \
		zip gammusca.zip gammusca
	mv /tmp/gammusca.zip $@


clean:
	rm -rf dist/*
	sbt clean
	rm -rf target

.PHONY: dist/gammusca.zip

