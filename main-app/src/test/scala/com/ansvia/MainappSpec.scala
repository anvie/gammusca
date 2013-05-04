package com.ansvia

import org.specs2.mutable._

class MainappSpec extends Specification {
	
	"Hello" should {
		"match 5 characters" in {
			"hello".length must equalTo(5)
		}
	}
	
}
