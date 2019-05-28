package gec.scf.dummy.common.serialize;

import com.fasterxml.jackson.databind.util.StdConverter;

import io.seruco.encoding.base62.Base62;

public class GecDecodeLongSerialize extends StdConverter<String, Long> {
	
	@Override
	public Long convert(String value) {
		Long result = null;
		if (value != null){
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(value.getBytes());
			value = new String(decoded);
			result = Long.parseLong(value);
		}
		return result;
	}

}
