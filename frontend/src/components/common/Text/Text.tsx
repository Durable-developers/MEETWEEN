import { ComponentPropsWithoutRef, ReactNode } from 'react';

import { SizeType } from '@type/size';

import { textStyle } from './Text.style';

export interface TextProps extends ComponentPropsWithoutRef<'p'> {
  size: Extract<SizeType, 'large' | 'medium' | 'small' | 'xSmall'>;
  children: ReactNode;
}

const Text = ({ size = 'medium', children, ...props }: TextProps) => {
  return (
    <p css={textStyle[size]} {...props}>
      {children}
    </p>
  );
};

export default Text;